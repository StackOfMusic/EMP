package com.emp.bpms.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.skt.bpms.model.MyCell;

import lombok.extern.slf4j.Slf4j; 

@Slf4j
@SpringBootApplication
public class ExcelPOIHelper { 
	
	
	/*
	 * ÎØ∏Íµ¨?òÑ ?Ç¨?ï≠(Static?úºÎ°? ?ïå?†§Î∞ïÏ? Í≤?)
	 * Border Color
	 * Font Style?ì§
	 * 
	 * 
	 */
	
	int[] columnWidthList;
	
    private static final Map<HorizontalAlignment, String> HALIGN = mapFor(
            HorizontalAlignment.LEFT, "left",
            HorizontalAlignment.CENTER, "center",
            HorizontalAlignment.RIGHT, "right",
            HorizontalAlignment.FILL, "left",
            HorizontalAlignment.JUSTIFY, "left",
            HorizontalAlignment.CENTER_SELECTION, "center");

    private static final Map<VerticalAlignment, String> VALIGN = mapFor(
            VerticalAlignment.BOTTOM, "bottom",
            VerticalAlignment.CENTER, "middle",
            VerticalAlignment.TOP, "top");

    private static final Map<BorderStyle, String> BORDER = mapFor(
            BorderStyle.DASH_DOT, "dashed 1pt black",
            BorderStyle.DASH_DOT_DOT, "dashed 1pt black",
            BorderStyle.DASHED, "dashed 1pt black",
            BorderStyle.DOTTED, "dotted 1pt black",
            BorderStyle.DOUBLE, "double 3pt black",
            BorderStyle.HAIR, "solid 1px black",
            BorderStyle.MEDIUM, "solid 2pt black",
            BorderStyle.MEDIUM_DASH_DOT, "dashed 2pt black",
            BorderStyle.MEDIUM_DASH_DOT_DOT, "dashed 2pt black",
            BorderStyle.MEDIUM_DASHED, "dashed 2pt black",
            BorderStyle.NONE, "none",
            BorderStyle.SLANTED_DASH_DOT, "dashed 2pt black",
            BorderStyle.THICK, "solid 3pt black",
            BorderStyle.THIN, "solid 1pt black");

    private static final int IDX_TABLE_WIDTH = -2;
    private static final int IDX_HEADER_COL_WIDTH = -1;


    @SuppressWarnings({"unchecked"})
    private static <K, V> Map<K, V> mapFor(Object... mapping) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < mapping.length; i += 2) {
            map.put((K) mapping[i], (V) mapping[i + 1]);
        }
        return map;
    }


    private <K> String styleReturn(String attr, K key, Map<K, String> mapping) {
        String value = mapping.get(key);
        if (value != null) {
            return String.format("  %s: %s;", attr, value);
        }
        return null;
    }

    public int[] getColumnWidthList() {
    	return this.columnWidthList;
    }
    
    private void setColumnWidthList(XSSFSheet sheet) {
    	columnWidthList = new int[sheet.getRow(1).getLastCellNum()+1];
    	for(int i = 0 ; i <= sheet.getRow(1).getLastCellNum() ; i++) {
    		columnWidthList[i] = (int)sheet.getColumnWidthInPixels(i);
    	}
    	
    }
    
    public Map<Integer, List<MyCell>> readExcel(InputStream is, String fileLocation) throws IOException { 
        Map<Integer, List<MyCell>> data = new HashMap<>(); 

        if (fileLocation.endsWith(".xls")) { 
            data = readHSSFWorkbook(is); 
        } else if (fileLocation.endsWith(".xlsx")) {
        	//Map<Integer, List<MyCell>> data = new HashMap<>();
            data = readXSSFWorkbook(is);
        	
        } 

       int maxNrCols = data.values().stream() 
          .mapToInt(List::size) 
          .max() 
          .orElse(0); 

        data.values().stream() 
          .filter(ls -> ls.size() < maxNrCols) 
          .forEach(ls -> { 
              IntStream.range(ls.size(), maxNrCols) 
                .forEach(i -> ls.add(new MyCell(""))); 
          }); 
        
        return data; 
    } 
	
    public String wrapHtmlExpression(String data, String tag) {
    	return wrapHtmlExpression(data, tag, null, 0, 0);
    }
    
    public String wrapHtmlExpression(String data, String tag, String style, int colspan, int rowspan) {
    	String tagStr = "<";
    	
    	
    	
    	if(style == null) {
    		tagStr = tagStr + tag;	
    	} else if(style == "null" || style == ""){
    		tagStr = tagStr + tag;
    	} else {
    		tagStr = tagStr + tag + " style='"+style+"'";
    	}
    	
    	if(rowspan > 0) {
    		tagStr = tagStr + " rowspan='"+rowspan+"'";
    	}
    	
    	if(colspan > 0) {
    		tagStr = tagStr + " colspan='"+colspan+"'";    		
    	}
    	
    	tagStr = tagStr + ">";
    	
    	return String.format("%s%s</%s>\n", tagStr, data, tag);
    }
    

	public String toHtml(Map<Integer, List<MyCell>> data) {
		String htmlString = "";
		//json ?åå?ã± ?ïÑ?öî
		for(List<MyCell> row : data.values()) {
			htmlString = htmlString + "<tr>\n";
			for(MyCell cell : row) {
				
				htmlString = htmlString + wrapHtmlExpression(cell.getContent(), "td", cell.getHtmlStyle(), cell.getColspan(), cell.getRowspan());
			}
			htmlString = htmlString + "</tr>\n";
		}
		
		return wrapHtmlExpression(htmlString, "table");
	}
	
	public String toHtml(String fileLocation) throws IOException {
		return toHtml(fileLocation, null);
	}

	
	public String toHtml(String fileLocation, String jsonData) throws IOException {
		InputStream is = new FileInputStream(fileLocation);
		return toHtml(is, jsonData);
	}
	
	public String toHtml(InputStream is) throws IOException {
		return toHtml(is, null);
	}
	
	public String toHtml(InputStream is, String jsonData) throws IOException {
		
		Map<Integer, List<MyCell>> data = readXSSFWorkbook(is);
		
		if(jsonData != null) {
			//json ?åå?ã±
			//json ?ï≠Î™©Î≥Ñ data?óê Í∞? ?Ñ£Í∏?
		}
		
		return toHtml(data);
	}
	
	public String toHtml(Map<Integer, List<MyCell>> data, String jsonData) {

		if(jsonData != null) {
			//json ?åå?ã±
			//json ?ï≠Î™©Î≥Ñ data?óê Í∞? ?Ñ£Í∏?
		}
		
		return toHtml(data);
	
	}
	
	
//*  
    public Map<Integer, List<MyCell>> readExcel(String fileLocation) throws IOException { 
    	InputStream is = new FileInputStream(fileLocation);
        return readExcel(is, fileLocation); 
    } 
//*/
    private String readCellContent(Cell cell) { 
        String content; 
        switch (cell.getCellTypeEnum()) { 
        case STRING: 
            content = cell.getStringCellValue();
            break; 
        case NUMERIC: 
            if (DateUtil.isCellDateFormatted(cell)) { 
            	content = cell.getDateCellValue() + "";
            } else { 
                content = cell.getNumericCellValue() + "";
            } 
            break; 
        case BOOLEAN: 
            content = cell.getBooleanCellValue() + ""; 
            break; 
        case FORMULA: 
            content = cell.getCellFormula() + ""; 
            break; 
        default: 
            content = ""; 
        } 
        return content; 
    } 


    private Map<Integer, List<MyCell>> readHSSFWorkbook(InputStream fis) throws IOException { 
        Map<Integer, List<MyCell>> data = new HashMap<>(); 
        HSSFWorkbook workbook = null; 
        try { 
            workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0); 
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) { 
                HSSFRow row = sheet.getRow(i); 
                data.put(i, new ArrayList<>()); 
                if (row != null) { 
                	
                    for (int j = 0; j < row.getLastCellNum(); j++) { 
                        HSSFCell cell = row.getCell(j); 

                        if (cell != null) { 
                            HSSFCellStyle cellStyle = cell.getCellStyle(); 
                            MyCell myCell = new MyCell(); 

                            HSSFColor bgColor = cellStyle.getFillForegroundColorColor(); 
                            if (bgColor != null) { 
                                short[] rgbColor = bgColor.getTriplet(); 
                                myCell.setBgColor("rgb(" + rgbColor[0] + "," + rgbColor[1] + "," + rgbColor[2] + ")"); 
                            } 
                            HSSFFont font = cell.getCellStyle() 
                                .getFont(workbook); 
                            myCell.setTextSize(font.getFontHeightInPoints() + ""); 
                            if (font.getBold()) { 
                                myCell.setTextWeight("bold"); 
                            } 
                            HSSFColor textColor = font.getHSSFColor(workbook); 
                            if (textColor != null) { 
                                short[] rgbColor = textColor.getTriplet(); 
                                myCell.setTextColor("rgb(" + rgbColor[0] + "," + rgbColor[1] + "," + rgbColor[2] + ")"); 
                            } 
                            myCell.setContent(readCellContent(cell)); 
                            data.get(i) 
                            .add(myCell); 
                        } else { 
                            data.get(i) 
                                .add(new MyCell("")); 
                        } 
                    } 
                } 
            } 
        } finally { 
            if (workbook != null) { 
                workbook.close(); 
            } 
        } 
        return data; 
    } 
    
    private String styleColor(String attr, XSSFColor color) {
        String tmpStr = "";
    	if (color == null || color.isAuto()) {
            return null;
        }

        byte[] argb = color.getARGB();
        if (argb != null) {
            tmpStr = String.format("%s: #%02x%02x%02x%02x;", attr,
                    argb[1], argb[2], argb[3], argb[0] );
            return tmpStr;
            
        }

        byte[] rgb = color.getRGB();
        if (rgb != null) {
            // This is done twice -- rgba is new with CSS 3, and browser that don't
            // support it will ignore the rgba specification and stick with the
            // solid color, which is declared first
            tmpStr = String.format("%s: #%02x%02x%02x;", attr, rgb[0], rgb[1], rgb[2]);
            return tmpStr;
        } 

        return null;
    } 
    
    private byte applyTint(int lum, double tint) {
        if (tint > 0) {
            return (byte) (lum * (1.0 - tint) + (255 - 255 * (1.0 - tint)));
        } else if (tint < 0) {
            return (byte) (lum * (1 + tint));
        } else {
            return (byte) lum;
        }
    }
    
    
    
    
    
    private Map<Integer, List<MyCell>> readXSSFWorkbook(String fileLocation) throws IOException {
    	InputStream fis = new FileInputStream(fileLocation);
    	return readXSSFWorkbook(fis);
    }
    
    private Map<Integer, List<MyCell>> readXSSFWorkbook(InputStream fis) throws IOException { 
        String htmlStyleStr;
    	XSSFWorkbook workbook = null;
        Map<Integer, List<MyCell>> data = new HashMap<>(); 
        try { 
        	workbook = new XSSFWorkbook(fis); 
            XSSFSheet sheet = workbook.getSheetAt(0); 
            String tmpStr;
            	
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) { 
                tmpStr = "";
            	XSSFRow row = sheet.getRow(i); 
                data.put(i, new ArrayList<>()); 
                if (row != null) { 
                    for (int j = 0; j < row.getLastCellNum(); j++) { 
                        XSSFCell cell = row.getCell(j); 
                        htmlStyleStr = "";
                        if (cell != null) { 
                        	
                            XSSFCellStyle cellStyle = cell.getCellStyle(); 
                            MyCell myCell = new MyCell(); 
                            
                            XSSFColor bgColor = cellStyle.getFillForegroundColorColor();
                            
                            if (bgColor != null) {
                            	
                            	byte[] rgbColor = bgColor.getRGB(); 

                                if(bgColor.getTint() != 0.0) {
                                	rgbColor[0] = applyTint(rgbColor[0] & 0xFF,bgColor.getTint());
                                	rgbColor[1] = applyTint(rgbColor[1] & 0xFF,bgColor.getTint());
                                	rgbColor[2] = applyTint(rgbColor[2] & 0xFF,bgColor.getTint());
                            		bgColor.setRGB(rgbColor);
                            	}
                            	
                            	myCell.setBgColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0]) + "," + (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + "," + (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")"); 


                                tmpStr = styleColor("background-color", bgColor);
                                if(tmpStr != null) {
                                	htmlStyleStr = htmlStyleStr + tmpStr;                                	
                                }
                                	//bgcolor HTML String Î≥??ôò
                                
                            } 
                            XSSFFont font = cellStyle.getFont(); 
                            myCell.setTextSize(font.getFontHeightInPoints() + ""); 
                            if (font.getBold()) { 
                                myCell.setTextWeight("bold"); 
                                
                                htmlStyleStr = htmlStyleStr + "font-weight:bold;";
                                //font ÍµµÍ≤å HTML String Î≥??ôò
                            } 
                            XSSFColor textColor = font.getXSSFColor(); 
                            if (textColor != null) { 
                                byte[] rgbColor = textColor.getRGB(); 

                                if(textColor.getTint() != 0.0) {
                                	rgbColor[0] = applyTint(rgbColor[0] & 0xFF,bgColor.getTint());
                                	rgbColor[1] = applyTint(rgbColor[1] & 0xFF,bgColor.getTint());
                                	rgbColor[2] = applyTint(rgbColor[2] & 0xFF,bgColor.getTint());
                            		textColor.setRGB(rgbColor);
                            	}
                                
                                myCell.setTextColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0]) + "," + (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + "," + (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")"); 
                                
                                tmpStr = styleColor("text-color", textColor);
                                if(tmpStr != null) {
                                	htmlStyleStr = htmlStyleStr + tmpStr;
                                	
                                }
                                //color HTML String Î≥??ôò

                            } 
                            
                            BorderStyle borderBottom = cellStyle.getBorderBottom();
                            if (borderBottom != null) {
                            	myCell.setBorderBottom(borderBottom);
                            	
                            	htmlStyleStr = htmlStyleStr + styleReturn("border-bottom", borderBottom, BORDER);
                            }
                            BorderStyle borderLeft = cellStyle.getBorderLeft();
                            if (borderLeft != null) {
                            	myCell.setBorderLeft(borderLeft);
                            	htmlStyleStr = htmlStyleStr + styleReturn("border-left", borderLeft, BORDER);
                            }
                            BorderStyle borderRight = cellStyle.getBorderRight();
                        	if (borderRight != null) {
                        		myCell.setBorderRight(borderRight);
                        		htmlStyleStr = htmlStyleStr + styleReturn("border-right", borderRight, BORDER);
                            }
                            BorderStyle borderTop = cellStyle.getBorderTop();
                            if (borderTop != null) {
                            	myCell.setBorderTop(borderTop);
                            	htmlStyleStr = htmlStyleStr + styleReturn("border-top", borderTop, BORDER);
                            }
                            HorizontalAlignment alignment = cellStyle.getAlignment();
                            if (alignment != null) {
                            	myCell.setAlignment(alignment);
                            	htmlStyleStr = htmlStyleStr + styleReturn("text-align",alignment, HALIGN);
                            }
                            //XSSFColor bordercolor = cellStyle.getBorderColor(null);
                            //HorizontalAlignment horizontalAlignment = cellStyle.getAlignment();
                            myCell.setHtmlStyle(htmlStyleStr);
                            myCell.setContent(readCellContent(cell)); 
                            data.get(i) 
                                .add(myCell); 
                        } else { 
                            data.get(i) 
                                .add(new MyCell("")); 
                        } 
                    } 
                } 
            } 
            
            executeMergeData(data, sheet.getMergedRegions(), sheet.getLastRowNum(), sheet.getRow(1).getLastCellNum());
            setColumnWidthList(sheet);
        } finally { 
            if (workbook != null) { 
                workbook.close(); 
                
            } 
            if (fis != null) {
            	fis.close();
            }
        } 
        
        return data; 
    } 

    private void executeMergeData(Map<Integer, List<MyCell>> data, List<CellRangeAddress> craList, int rowSize, int columnSize) {
    	//List<CellRangeAddress> craList = sheet.getMergedRegions();
    	
    	Collections.sort(craList, new Comparator<CellRangeAddress>() {
    		@Override
    		public int compare(CellRangeAddress c1, CellRangeAddress c2) {
    			if(c1.getLastRow() > c2.getLastRow()) {
    				return 1;
    			} else if(c1.getLastRow() == c2.getLastRow()) {
    				if(c1.getLastColumn() > c2.getLastColumn()) {
    					return 1;
    				} else if(c1.getLastColumn() == c2.getLastColumn()) {
    					return 0;
    				} else {
    					return -1;
    				}
    			} else {
    				return -1;
    			}
    		}
    	});

    	Collections.reverse(craList);
    	int[][] checkList = new int[rowSize+1][columnSize+1];
    	for(int[] list : checkList) {
    		Arrays.fill(list,  1);
    	}
    	
    	MyCell targetCell;
    	MyCell endCell;
        BorderStyle borderBottom;
        BorderStyle borderRight;
        //cell region area ?†ï?†¨
    	for(CellRangeAddress range : craList) {
    		//???ÉÅ Ï≤? ?? ?Ñ†?†ï -> span ?†ïÎ≥? ?ûÖ?†• -> ?Öå?ëêÎ¶? ?†ïÎ≥? ?ûÖ?†•(?ò§Î•∏Ï™Ω ?úÑ-right, ?ò§Î•∏Ï™Ω ?ïÑ?ûò-bottom)
    		targetCell = data.get(range.getFirstRow()).get(range.getFirstColumn());
    		endCell = data.get(range.getLastRow()).get(range.getLastColumn());
    		//Span ?Ñ§?†ï
    		if(range.getLastColumn() - range.getFirstColumn() > 0) {
    			targetCell.setColspan((range.getLastColumn() - range.getFirstColumn())+1);
    		}
    		if(range.getLastRow() - range.getFirstRow() > 0) {
    			targetCell.setRowspan((range.getLastRow() - range.getFirstRow())+1);
    		}
    		//?Öå?ëêÎ¶? Ïß??†ï
            borderBottom = endCell.getBorderBottom();
            if (borderBottom != null) {
            	targetCell.setBorderBottom(borderBottom);
            	targetCell.setHtmlStyle(targetCell.getHtmlStyle() + styleReturn("border-bottom", borderBottom, BORDER));
            }
            borderRight = endCell.getBorderRight();
        	if (borderRight != null) {
        		targetCell.setBorderRight(borderRight);
        		targetCell.setHtmlStyle(targetCell.getHtmlStyle() + styleReturn("border-right", borderRight, BORDER));	
        	}
    		//Cell Íµ¨Ï°∞ ?ôï?†ï?ùÑ ?úÑ?ïú Í≥µÏÇ¨
        	for( int i = range.getFirstRow() ; i <= range.getLastRow() ; i++) {
        		for(int j = range.getFirstColumn() ; j <= range.getLastColumn() ; j++) {
        			checkList[i][j] = 0;
        		}        		
        	}
        	checkList[range.getFirstRow()][range.getFirstColumn()] = 1;
    	}
    	for(int i = rowSize ; i >= 0 ; i--) {
    		List<MyCell> rowList = data.get(i);
    		for(int j = columnSize ; j >= 0 ; j--) {
    			if(checkList[i][j] == 0) {
    				rowList.remove(j);
    			}
    		}
    	}
    	
    	
    
    }
}
