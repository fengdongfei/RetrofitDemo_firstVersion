package com.feng.retrofit.model;

import com.feng.retrofit.api.MResponse;
import com.feng.retrofit.api.MResult;

import java.io.Serializable;

/**
 * Created by Chexiangjia-MAC on 17/6/26.
 */

public class BModel<T extends Serializable> extends MResponse implements Serializable,MResult {

}
