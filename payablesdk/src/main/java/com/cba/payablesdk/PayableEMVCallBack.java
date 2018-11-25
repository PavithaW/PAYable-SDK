package com.cba.payablesdk;

import models.TxSaleRes;

/**
 * Created by Dell on 1/9/2017.
 */

interface PayableEMVCallBack {

    public void onSuccessSales(TxSaleRes txSaleRes);

    public void onFailureSales(PayableException e);
}
