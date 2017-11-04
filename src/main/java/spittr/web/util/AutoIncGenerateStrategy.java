package spittr.web.util;

import java.util.Random;

/**
 * 主键自动增长（+1）的生成策略
 * Created by ThinkPad on 2017/1/5.
 */
public class AutoIncGenerateStrategy extends PkGenerateStrategy {

    @Override
    public String GeneratePk(String oldPk) {
        if(StringUtil.IsNull(oldPk))
        {
            return getOriginalPk();
        }
        else
        {
            return  getNextPk(oldPk);
        }
    }

    /**
     * 获得原始的主键，以默认18位"0"填充
     * @return
     */
    private String getOriginalPk()
    {
        StringBuffer str=new StringBuffer();
        for (int i=0;i<primaryKeyLength;i++){
            str.append("0");
        }
        return str.toString();
    }

    /**
     * 获得下一位Pk
     * @param oldPk
     * @return
     * @throws Exception
     */
    private String getNextPk(String oldPk)
    {
        String newPk=null;
        try
        {
            Long pkDouble = new Long(oldPk);
            newPk=String.valueOf(pkDouble+1);
        }
        catch (Exception e)
        {
            // TODO: 2017/1/6 待完善主键冲突策略
            newPk=getRandomString();
        }
        return newPk;
    }

    /**
     * 获得随机字符
     * @return
     */
    private String getRandomString()
    {
//        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < primaryKeyLength; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
