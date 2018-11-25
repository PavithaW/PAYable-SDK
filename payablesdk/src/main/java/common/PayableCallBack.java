package common;


import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

/**
 * Created by Dell on 12/1/2015.
 */
public interface PayableCallBack {

    public void onSuccessEcho(int res);

    public void onFailureEcho(PayableException e);

    public void onSuccessOpenTx(ArrayList<PayableTX> res);

    public void onFailureOpenTx(PayableException e);

    public void onSuccessCloseTx(ArrayList<PayableTX> res);

    public void onFailureCloseTx(PayableException e);

    public void onSuccessSales(TxSaleRes txSaleRes);

    public void onFailureSales(PayableException e);

    public void onSuccessVoid(TxVoidRes txVoidRes);

    public void onFailureVoid(PayableException e);

    public void onSuccessSignature();

    public void onFailureSignature(PayableException e);

    public void onSuccessSettlementSummary(ArrayList<TxSettlementSummaryEle> res);

    public void onFailureSettlementSummary(PayableException e);

    public void onSuccessSettle(TxSettlementResponse res);

    public void onFailureSettle(PayableException e);

    public void onSuccessBatchList(ArrayList<BatchlistRes> res);

    public void onFailureBatchList(PayableException e);

    public void onSucessAuthenticate() ;

    public void onFailureAuthenticate(PayableException e);


}
