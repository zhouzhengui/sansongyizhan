package cn.stylefeng.guns.modular.suninggift.easyExcel.easyExcelHandler;

import com.alibaba.excel.event.WriteHandler;
import org.apache.poi.ss.usermodel.*;

/**
 * 通用报表样式
 */
public class CommExcelHandler implements WriteHandler {

    private CellStyle lightWhite = null;
    private CellStyle tan = null;
    private Font boldFont = null;

    /**
     * 表样式
     * @param sheetNo
     * @param sheet
     */
    @Override
    public void sheet(int sheetNo, Sheet sheet) {
        // TODO Auto-generated method stub
    }

    /**
     * 行样式
     * @param rowNum
     * @param row
     */
    @Override
    public void row(int rowNum, Row row) {
        // TODO Auto-generated method stub
        row.setHeight((short) 800);
    }

    /**
     * 单元格样式
     * @param i
     * @param cell
     */
    @Override
    public void cell(int i, Cell cell) {
        // TODO Auto-generated method stub
        // 从第二行开始设置格式，第一行是表头

        if(lightWhite == null || tan == null || boldFont == null){
            Workbook workbook = cell.getSheet().getWorkbook();
            lightWhite = createStyle(workbook, IndexedColors.WHITE.getIndex());
            tan = createStyle(workbook, IndexedColors.TAN.getIndex());
            boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldFont.setFontHeightInPoints((short) 11);
        }

        Sheet sheet = cell.getSheet();
        if(i == 0) {//第一行标题
            tan.setFont(boldFont);
            tan.setWrapText(true);
            cell.setCellStyle(tan);
        }else {
            // 内容样式
            cell.setCellStyle(lightWhite);
        }
    }


    /**
     * 实际中如果直接获取原单元格的样式进行修改, 最后发现是改了整行的样式, 因此这里是新建一个样* 式
     */
    private CellStyle createStyle(Workbook workbook, short colorIndex) {
        CellStyle cellStyle = workbook.createCellStyle();
        // 下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 背景颜色
        cellStyle.setFillForegroundColor(colorIndex);
        // 填充方式
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }



}
