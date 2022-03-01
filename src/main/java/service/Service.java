package service;

import result.Result;
import request.Request;

public interface Service {
    public Result processRequest(Request request);
}
