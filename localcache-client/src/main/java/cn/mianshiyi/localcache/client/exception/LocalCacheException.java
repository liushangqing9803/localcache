package cn.mianshiyi.localcache.client.exception;

/**
 * @author shangqing.liu
 */
public class LocalCacheException extends RuntimeException {
    private static final long serialVersionUID = -2924646262701911134L;

    public LocalCacheException(String message) {
        super(message);
    }

    public LocalCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalCacheException(Throwable cause) {
        super(cause);
    }

    public LocalCacheException() {

    }
}
