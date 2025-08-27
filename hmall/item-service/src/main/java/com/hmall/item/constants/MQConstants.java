package com.hmall.item.constants;

public interface MQConstants {
    String SEARCH_EXCHANGE_NAME = "search.direct";
    String ITEM_UPDATE_QUEUE_NAME = "search.item.update.queue";
    String ITEM_INDEX_QUEUE_NAME = "search.item.index.queue";
    String ITEM_DELETE_QUEUE_NAME = "search.item.delete.queue";
    String ITEM_STATES_QUEUE_NAME = "search.item.states.queue";
    String ITEM_UPDATE_KEY = "item.update";
    String ITEM_INDEX_KEY = "item.index";
    String ITEM_DELETE_KEY = "item.delete";
    String ITEM_UPDATESTATES_KEY = "item.updateStates";
}