/**
 * Copyright (c) 2015-present, Facebook, Inc. All rights reserved.
 * <p>
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary
 * form for use in connection with the web services and APIs provided by
 * Facebook.
 * <p>
 * As with any software that integrates with the Facebook platform, your use
 * of this software is subject to the Facebook Developer Principles and
 * Policies [http://developers.facebook.com/policy/]. This copyright notice
 * shall be included in all copies or substantial portions of the software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.facebook.ads.sdk;

import com.facebook.ads.utils.QueryParameterUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APINodeList<T extends APINode> extends ArrayList<T> implements APIResponse {
    private String before;
    private String after;

    private String afterId;
    private Integer offset;
    private Integer limit;
    private APIRequest<T> request;
    private String rawValue;

    public APINodeList(APIRequest<T> request, String rawValue) {
        this.request = request;
        this.rawValue = rawValue;
    }

    public APINodeList<T> nextPage() throws APIException {
        return nextPage(0);
    }

    public APINodeList<T> nextPage(int limit) throws APIException {
        if (after == null && afterId == null) {
            return null;
        }
        Map<String, Object> extraParams = new HashMap<String, Object>();
        if (limit > 0) {
            this.limit = limit;
        }

        if (this.limit != null) {
            extraParams.put("limit", this.limit);
        }
        if (this.after != null) {
            extraParams.put("after", after);
        } else if (this.afterId != null && this.offset != null) {
            extraParams.put("__after_id", afterId);
            extraParams.put("offset", this.offset);
        }
        return (APINodeList<T>) request.execute(extraParams);
    }

    @Deprecated
    public void setPaging(String before, String after) {
        this.before = before;
        this.after = after;
    }

    public void setPaging(JsonObject paging) {
        if (paging != null) {
            if (paging.has("cursors") && paging.get("cursors").isJsonObject()) {
                paging = paging.get("cursors").getAsJsonObject();
            }

            if (paging.has("next")) {
                Map<String, String> queryParam = QueryParameterUtil.getQueryMap(paging.get("next").getAsString());
                if (queryParam.containsKey("__after_id")) {
                    this.afterId = queryParam.get("__after_id");
                    this.offset = Integer.valueOf(queryParam.get("offset"));
                    this.limit = Integer.valueOf(queryParam.get("limit"));
                }
            } else {
                this.before = paging.has("before") ? paging.get("before").getAsString() : null;
                this.after = paging.has("after") ? paging.get("after").getAsString() : null;
            }
        }
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }

    public String getAfterId() {
        return afterId;
    }

    public Integer getOffset() {
        return offset;
    }

    @Override
    public String getRawResponse() {
        return rawValue;
    }

    @Override
    public JsonObject getRawResponseAsJsonObject() {
        JsonParser parser = new JsonParser();
        return parser.parse(rawValue).getAsJsonObject();
    }

    @Override
    public T head() {
        return this.size() > 0 ? this.get(0) : null;
    }

    @Override
    public APIException getException() {
        return null;
    }
}
