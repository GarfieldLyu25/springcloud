package com.hmall.search.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.search.domain.po.ItemDoc;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class itemListener {
    private final RestHighLevelClient client;
    private final ItemClient itemClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.index.queue",durable = "true"),
            exchange = @Exchange(name = "search.direct"),
            key = "item.index"
    ))
    public void listenItemIndex(Long itemId) throws IOException {
        ItemDTO itemDTO = itemClient.queryItemById(itemId);
        ItemDoc itemDoc = BeanUtil.copyProperties(itemDTO, ItemDoc.class);
        IndexRequest request = new IndexRequest("items").id(itemDoc.getId());
        request.source(JSONUtil.toJsonStr(itemDoc), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.states.queue",durable = "true"),
            exchange = @Exchange(name = "search.direct"),
            key = "item.updateStates"
    ))
    public void listenItemState(Long id) throws IOException {
        //下架商品
        ItemDTO itemDTO = itemClient.queryItemById(id);
        ItemDoc itemDoc = BeanUtil.copyProperties(itemDTO, ItemDoc.class);
        itemDoc.setUpdateTime(LocalDateTime.now());
        UpdateRequest request = new UpdateRequest("items", itemDoc.getId());
        request.doc(JSONUtil.toJsonStr(itemDoc), XContentType.JSON);
        client.update(request, RequestOptions.DEFAULT);

    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue",durable = "true"),
            exchange = @Exchange(name = "search.direct"),
            key = "item.delect"
    ))
    public void listenItemDelete(Long id) throws IOException {
        DeleteRequest request = new DeleteRequest("items").id(id.toString());
        client.delete(request, RequestOptions.DEFAULT);
    }
}
