package cn.mianshiyi.localcache.client.exception;


/**
 * @author shangqing.liu
 */
public class DistLocalCacheRuntimeException extends RuntimeException {
    public DistLocalCacheRuntimeException() {
    }

    public DistLocalCacheRuntimeException(String message) {
        super(message);
    }

    public DistLocalCacheRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistLocalCacheRuntimeException(Throwable cause) {
        super(cause);
    }

    protected DistLocalCacheRuntimeException(String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
