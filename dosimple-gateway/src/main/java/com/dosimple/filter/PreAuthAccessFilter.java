package com.dosimple.filter;


/**
 * @date: 2018/7/18 0018 上午 11:47
 * 1.5版本zuul的过滤器
 */
@Deprecated
public class PreAuthAccessFilter{
    /*public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    public int filterOrder() {
        return 100;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                log.info("head[name={},value={}]", name, value);
            }
        }
        ctx.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials("user", "123456"));
        return null;
    }
    private String getBase64Credentials(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }*/
}
