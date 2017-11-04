package spittr.web.util;

/**
 * 主键生成策略
 * Created by ThinkPad on 2017/1/5.
 */
public abstract class PkGenerateStrategy {
    protected  int primaryKeyLength=20;   /*主键长度  固定为18位*/
    /**
     * 生成下一位主键
     * @param oldPk
     * @return
     */
   public abstract String GeneratePk(String oldPk);
}
