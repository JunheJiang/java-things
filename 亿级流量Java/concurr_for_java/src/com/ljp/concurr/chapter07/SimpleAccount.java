package com.ljp.concurr.chapter07;

import java.math.BigDecimal;

//用户银行账号模拟类
public class SimpleAccount {

    private String accountId;
    private BigDecimal totalAccount;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(BigDecimal totalAccount) {
        this.totalAccount = totalAccount;
    }

}
