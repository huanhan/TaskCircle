package com.tc.dto.finance;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.DateType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;

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



        return null;
    }
}
