package com.taotao.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-10-25 14:48
 */
public class DynWordUtils {

    private final Logger logger = LoggerFactory.getLogger(DynWordUtils.class);

    /**
     * 被list替换的段落 被替换的都是oldParagraph
     */
    private XWPFParagraph oldParagraph;

    /**
     * 参数
     */
    private Map<String, Object> paramMap;

    /**
     * 当前元素的位置
     */
    int n = 0;

    /**
     * 判断当前是否是遍历的表格
     */
    boolean isTable = false;

    /**
     * 模板对象
     */
    XWPFDocument templateDoc;

    /**
     * 默认字体的大小
     */
    final int DEFAULT_FONT_SIZE = 10;

    /**
     * 重复模式的占位符所在的行索引
     */
    private int currentRowIndex;

    /**
     * 入口
     *
     * @param paramMap     模板中使用的参数
     * @param templatePaht 模板全路径
     * @param outPath      生成的文件存放的本地全路径
     */
    public static void process(Map<String, Object> paramMap, String templatePaht, String outPath) {
        DynWordUtils dynWordUtils = new DynWordUtils();
        dynWordUtils.setParamMap(paramMap);
        dynWordUtils.createWord(templatePaht, outPath);
    }

    /**
     * 生成动态的word
     * @param templatePath
     * @param outPath
     */
    public void createWord(String templatePath, String outPath) {
        try (FileOutputStream outStream = new FileOutputStream(outPath)) {
            InputStream inputStream = new FileInputStream(ResourceUtils.getFile(templatePath));
            templateDoc = new XWPFDocument(OPCPackage.open(inputStream));
            parseTemplateWord();
            templateDoc.write(outStream);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            String className = stackTrace[0].getClassName();
            String methodName = stackTrace[0].getMethodName();
            int lineNumber = stackTrace[0].getLineNumber();

            throw new RuntimeException(e.getCause().getMessage());
        }
    }

    /**
     * 解析word模板
     */
    public void parseTemplateWord() throws Exception {

        List<IBodyElement> elements = templateDoc.getBodyElements();

        for (; n < elements.size(); n++) {
            IBodyElement element = elements.get(n);
            // 普通段落
            if (element instanceof XWPFParagraph) {

                XWPFParagraph paragraph = (XWPFParagraph) element;
                oldParagraph = paragraph;
                if (paragraph.getParagraphText().isEmpty()) {
                    continue;
                }

               // delParagraph(paragraph);

            } else if (element instanceof XWPFTable) {
                // 表格
                isTable = true;
                XWPFTable table = (XWPFTable) element;

               // delTable(table, paramMap);
                isTable = false;
            }
        }

    }




    /**
     * 处理的动态的段落（参数为list）
     *
     * @param placeholder 段落占位符
     * @param obj
     * @return
     */
    private String delDynList(String placeholder, List obj) {
        String placeholderValue = placeholder;
        List dataList = obj;
        Collections.reverse(dataList);
        for (int i = 0, size = dataList.size(); i < size; i++) {
            Object text = dataList.get(i);
            // 占位符的那行, 不用重新创建新的行
            if (i == 0) {
                placeholderValue = String.valueOf(text);
            } else {
                XWPFParagraph paragraph = createParagraph(String.valueOf(text));
                if (paragraph != null) {
                    oldParagraph = paragraph;
                }
                // 增加段落后doc文档的element的size会随着增加（在当前行的上面添加
                // 这里减操作是回退并解析新增的行（因为可能新增的带有占位符，这里为了支持图片和表格）
                if (!isTable) {
                    n--;
                }
            }
        }
        return placeholderValue;
    }

    /**
     * 创建段落 <p></p>
     *
     * @param texts
     */
    public XWPFParagraph createParagraph(String... texts) {

        // 使用游标创建一个新行
        XmlCursor cursor = oldParagraph.getCTP().newCursor();
        XWPFParagraph newPar = templateDoc.insertNewParagraph(cursor);
        // 设置段落样式
        newPar.getCTP().setPPr(oldParagraph.getCTP().getPPr());

        copyParagraph(oldParagraph, newPar, texts);

        return newPar;
    }




    /**
     * 删除模板行
     */
    private void deleteTemplateRow(int startRowIdx, int endRowIdx, XWPFTable table) {
        for (;startRowIdx <= endRowIdx; startRowIdx++) {
            table.removeRow(0);
        }
    }



    /**
     * 复制段落
     *
     * @param sourcePar 原段落
     * @param targetPar
     * @param texts
     */
    private void copyParagraph(XWPFParagraph sourcePar, XWPFParagraph targetPar, String... texts) {

        targetPar.setAlignment(sourcePar.getAlignment());
        targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

        // 设置布局
        targetPar.setAlignment(sourcePar.getAlignment());
        targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

        if (texts != null && texts.length > 0) {
            String[] arr = texts;
            XWPFRun xwpfRun = sourcePar.getRuns().size() > 0 ? sourcePar.getRuns().get(0) : null;

            for (int i = 0, len = texts.length; i < len; i++) {
                String text = arr[i];
                XWPFRun run = targetPar.createRun();

                run.setText(text);

                run.setFontFamily(xwpfRun.getFontFamily());
                int fontSize = xwpfRun.getFontSize();
                run.setFontSize((fontSize == -1) ? DEFAULT_FONT_SIZE : fontSize);
                run.setBold(xwpfRun.isBold());
                run.setItalic(xwpfRun.isItalic());
            }
        }
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
