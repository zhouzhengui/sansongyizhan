package cn.stylefeng.guns.modular.suninggift.easyExcel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EasyExcelService {



    /**
     * 包含model和样式
     * @param data
     * @param clazz
     * @param handler
     * @param excelTypeEnum
     * @param sheetName
     */
    public Map<String,Object> writeExcelWithModelAndHandler(List<? extends BaseRowModel> data,
                                                            Class<? extends BaseRowModel> clazz, WriteHandler handler,
                                                            Map<Integer,Integer> columnWidthMap, ExcelTypeEnum excelTypeEnum,
                                                            String sheetName,OutputStream out) {

        Map<String,Object> returnResult = new HashMap<>();
        returnResult.put("status","1");
        returnResult.put("msg","");

        if(data == null || data.size() == 0){
            returnResult.put("status","0");
            returnResult.put("msg","无数据，导出失败");
            return returnResult;
        }
        //这里指定需要表头，因为model通常包含表头信息
        ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null, out,
                excelTypeEnum, true, handler);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0, clazz);
        sheet1.setSheetName(sheetName);
        sheet1.setColumnWidthMap(columnWidthMap);
        writer.write(data,sheet1);
        writer.finish();
        try{
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            log.error("IO异常",e);
        }

        return returnResult;
    }

}
