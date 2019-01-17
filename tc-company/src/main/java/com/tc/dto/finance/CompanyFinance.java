package com.tc.dto.finance;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.DateType;
import com.tc.db.enums.WithdrawType;
import com.tc.until.FloatHelper;
import com.tc.until.TimestampHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 公司财务详情
 * @author Cyg
 */
public class CompanyFinance extends PageRequest {

    private String name;
    private Timestamp begin;
    private Timestamp end;
    /**
     * 总收入，等于用户充值总额
     */
    private Float in;
    /**
     * 总支出，等于用户提现总额
     */
    private Float out;
    /**
     * 实际收入，等于用户提现时的提成额度
     */
    private Float actualIn;

    public CompanyFinance() {
        super(0,12);
    }

    public CompanyFinance(String name, Timestamp begin, Timestamp end, Float in, Float out, Float actualIn) {
        super(0, 12);
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.in = in;
        this.out = out;
        this.actualIn = actualIn;
    }

    public CompanyFinance(int page, int size) {
        super(page, size);
    }

    public CompanyFinance(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public CompanyFinance(int page, int size, Sort sort) {
        super(page, size, sort);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Float getIn() {
        return in;
    }

    public void setIn(Float in) {
        this.in = in;
    }

    public Float getOut() {
        return out;
    }

    public void setOut(Float out) {
        this.out = out;
    }

    public Float getActualIn() {
        return actualIn;
    }

    public void setActualIn(Float actualIn) {
        this.actualIn = actualIn;
    }


    public static List<CompanyFinance> getBy(List<UserWithdraw> content, DateType type) {
        List<CompanyFinance> result = new ArrayList<>();

        content.forEach(userWithdraw -> {
            AtomicBoolean exist = new AtomicBoolean(false);
            result.forEach(companyFinance -> {
                if (companyFinance.name.equals(toName(userWithdraw.getAuditPassTime(),type))){
                    //如果在结果集中存在相同名称的记录，则进行汇总

                    exist.set(true);

                    if (userWithdraw.getType().equals(WithdrawType.PAY)) {
                        //总收入由用户充值而来
                        //统计总收入
                        companyFinance.setIn(FloatHelper.add(companyFinance.getIn(), userWithdraw.getMoney()));

                    }else {
                        //总支出与实际收入由用户提现而产生
                        //统计总支出
                        companyFinance.setOut(FloatHelper.add(companyFinance.getOut(), userWithdraw.getMoney()));
                        //统计实际收入
                        companyFinance.setActualIn(
                                FloatHelper
                                        .subToBD(userWithdraw.getMoney(),userWithdraw.getRealityMoney())
                                        .add(new BigDecimal(companyFinance.getActualIn().toString()))
                                        .floatValue()
                        );
                    }
                }
            });
            if (!exist.get()){
                //如果在结果集中不存在，则新增一条
                result.add(
                    new CompanyFinance(
                            toName(userWithdraw.getAuditPassTime(),type),
                            toTimestamp(userWithdraw.getAuditPassTime(),type,true),
                            toTimestamp(userWithdraw.getAuditPassTime(),type,false),
                            userWithdraw.getType().equals(WithdrawType.PAY) ? userWithdraw.getMoney() : 0,
                            userWithdraw.getType().equals(WithdrawType.WITHDRAW) ? userWithdraw.getMoney() : 0,
                            userWithdraw.getType().equals(WithdrawType.WITHDRAW) ? FloatHelper.sub(userWithdraw.getMoney(),userWithdraw.getRealityMoney()) : 0
                    )
                );
            }
        });


        return result;
    }


    private static Timestamp toTimestamp(Timestamp auditPassTime,DateType type,boolean isBegin){
        Timestamp result = null;
        switch (type){
            case DAY:
                result = isBegin ? TimestampHelper.toDayBegin(auditPassTime) : TimestampHelper.toDayEnd(auditPassTime);
                break;
            case MONTH:
                result = isBegin ? TimestampHelper.toMonthBegin(auditPassTime) : TimestampHelper.toMonthEnd(auditPassTime);
                break;
            case YEAR:
                result = isBegin ? TimestampHelper.toYearBegin(auditPassTime) : TimestampHelper.toYearEnd(auditPassTime);
                break;
            default:
                break;
        }
        return result;
    }

    private static String toName(Timestamp timestamp,DateType type){
        String name = "";
        switch (type){
            case DAY:
                if (TimestampHelper.isToday(timestamp)){
                    name = "今天";
                }else {
                    name = TimestampHelper.toLocalDate(timestamp).toString();
                }
                break;
            case MONTH:
                name = TimestampHelper.toMonth(timestamp);
                break;
            case YEAR:
                name = TimestampHelper.toYear(timestamp);
                break;
            default:
                break;
        }
        return name;
    }

}
