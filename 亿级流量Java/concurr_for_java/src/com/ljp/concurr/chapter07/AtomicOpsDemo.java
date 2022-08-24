package com.ljp.concurr.chapter07;

import java.math.BigDecimal;

public class AtomicOpsDemo {
    public static void main(String[] args){
        SimpleAccount account001 = new SimpleAccount();
        SimpleAccount account002 = new SimpleAccount();

        account001.setAccountId("A001");
        account001.setTotalAccount(new BigDecimal(65000.00));

        account002.setAccountId("B001");
        account002.setTotalAccount(new BigDecimal(30000.00) );

        System.out.println("账户" + account001.getAccountId()
                + ", 现在金额：" + account001.getTotalAccount());
        System.out.println("账户" + account002.getAccountId()
                + ", 现在金额：" + account002.getTotalAccount());

        System.out.println("账户" + account001.getAccountId() + "向账户"
                + account002.getAccountId() + "转账20000");
        transferFromFirstAccountToSecondAccount(account001, account002,
                new BigDecimal(20000));

        System.out.println("账户" + account001.getAccountId()
                + ", 现在金额：" + account001.getTotalAccount());
        System.out.println("账户" + account002.getAccountId()
                + ", 现在金额：" + account002.getTotalAccount());

        for (int i=0; i<1000; i++){
            transferFromFirstAccountToSecondAccount(account002, account001,
                    new BigDecimal(20000));
            transferFromFirstAccountToSecondAccount(account001, account002,
                    new BigDecimal(20000));
        }


    }

    //synchronized关键字定义了一个原子操作方法，哪怕
    //内部是分为多个步骤，但实际上还是一个原子操作。
    public static synchronized boolean
                transferFromFirstAccountToSecondAccount(SimpleAccount fAccount,
                SimpleAccount sAccount, BigDecimal money){
        //以下是转账的核心代码，
        //分两步，（1）账户1减少相应金额，（2）账户2增加相应金额
        fAccount.setTotalAccount(fAccount.getTotalAccount().subtract(money));
        sAccount.setTotalAccount(sAccount.getTotalAccount().add(money));
        return true;

    }
}
