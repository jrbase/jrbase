package org.github.jrbase.service;

import io.grpc.stub.StreamObserver;
import org.github.jrbase.db.CacheData;
import org.github.jrbase.proto.CmdServiceGrpc;
import org.github.jrbase.proto.Request;
import org.github.jrbase.proto.Response;

public class CmdImpl extends CmdServiceGrpc.CmdServiceImplBase {
        private CacheData cacheData = CacheData.getInstance();

        @Override
        public void requestCmd(Request request, StreamObserver<Response> responseObserver) {
            String result;
            int cmd = request.getCmd();
            if (-1 == cmd) {
                return;
            }
            String key = request.getKey();
            switch (cmd) {
                case 1: {
                    String value = request.getValueStr();
                    cacheData.getMapCache().put(key, value);
                    result = "true";
                    break;
                }
                case 2: {
                    result = (cacheData.getMapCache().get(key));
                    break;
                }
                default:
                    result = "failed";
            }
            Response reply = Response.newBuilder().setValue(result).build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }