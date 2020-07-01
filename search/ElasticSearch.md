#### 新增index

PUT product

```json
{
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId": {
        "type": "keyword"
      },
      "skuTitle": {
        "type": "text"
      },
      "price": {
        "type": "keyword"
      },
      "skuDefaultImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "saleCount": {
        "type": "long"
      },
      "hasStock": {
        "type": "boolean"
      },
      "hotScore": {
        "type": "long"
      },
      "brandId": {
        "type": "long"
      },
      "catalogId": {
        "type": "long"
      },
      "brandName": {
        "type": "keyword"
      },
      "brandImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "categoryName": {
        "type": "keyword"
      },
      "attrs": {
        "type": "nested",
        "properties": {
          "attrId": {
            "type": "long"
          },
          "attrName": {
            "type": "keyword"
          },
          "attrValue": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
```

#### 查询index
GET product/_mapping

#### 删除index
DELETE product

#### 查询所有

GET product/_search

```json
{
  "query": {
    "match_all": {}
  }
}
```

- 模糊匹配

- 过滤(属性, 分类, 品牌, 价格区间, 库存)

- 排序

- 分页

- 高亮

- 聚合分析

**如果是嵌入式的属性, 则查询与聚合都需要使用嵌入式**

#### 查询DSL

GET product/_search

```json
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "skuTitle": "黑色"
          }
        }
      ],
      "filter": [
        {
          "term": {
            "catalogId": "225"
          }
        },
        {
          "term": {
            "hasStock": "true"
          }
        },
        {
          "terms": {
            "brandId": [
              "1",
              "2"
            ]
          }
        },
        {
          "nested": {
            "path": "attrs",
            "query": {
              "bool": {
                "must": [
                  {
                    "term": {
                      "attrs.attrId": {
                        "value": "7"
                      }
                    }
                  },
                  {
                    "terms": {
                      "attrs.attrValue": [
                        "支持"
                      ]
                    }
                  }
                ]
              }
            }
          }
        },
        {
          "range": {
            "price": {
              "gte": 0,
              "lte": 2999
            }
          }
        }
      ]
    }
  },
  "sort": [
    {
      "price": {
        "order": "desc"
      }
    }
  ],
  "from": 0,
  "size": 2,
  "highlight": {
    "fields": {
      "skuTitle": {}
    },
    "pre_tags": "<b stype='color:red'>",
    "post_tags": "</b>"
  },
  "aggs": {
    "brand_agg": {
      "terms": {
        "field": "brandId",
        "size": 10
      },
      "aggs": {
        "brand_name_agg": {
          "terms": {
            "field": "brandName",
            "size": 10
          }
        }
      }
    },
    "catalog_agg": {
      "terms": {
        "field": "catalogId",
        "size": 10
      },
      "aggs": {
        "catalog_name_agg": {
          "terms": {
            "field": "categoryName",
            "size": 10
          }
        }
      }
    },
    "attr_agg": {
      "nested": {
        "path": "attrs"
      },
      "aggs": {
        "attr_id_agg": {
          "terms": {
            "field": "attrs.attrId",
            "size": 10
          },
          "aggs": {
            "attr_name_agg": {
              "terms": {
                "field": "attrs.attrName",
                "size": 10
              }
            },
            "attr_value_agg": {
              "terms": {
                "field": "attrs.attrValue",
                "size": 10
              }
            }
          }
        }
      }
    }
  }
}
```

#### 删除文档

DELETE product/_doc/1