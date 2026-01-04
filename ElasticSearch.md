## Elasticsearch 검색용 색인을 위한 배치 구성

한글 색인을 위한 elasticsearch 색인 설정 및 초기 인덱스 설정

### standard 적용
PUT search-v1
```json
{
  "settings": {
    "index": {
      "number_of_shards": 1,
      "number_of_replicas": 0
    }
  },
  "mappings": {
    "dynamic": "false",
    "properties": {
      "id": { "type": "long" },

      "productName": {
        "type": "text",
        "analyzer": "standard",
        "fields": {
          "keyword": { "type": "keyword", "ignore_above": 256 }
        }
      },

      "price": { "type": "long" },
      "quantity": { "type": "integer" },

      "description": {
        "type": "text",
        "analyzer": "standard"
      },

      "categoryId": { "type": "long" },

      "categoryName": {
        "type": "text",
        "analyzer": "standard",
        "fields": {
          "keyword": { "type": "keyword", "ignore_above": 256 }
        }
      },

      "indexedAt": {
        "type": "date",
        "format": "date_optional_time||epoch_millis"
      }
    }
  }
}
```

<br>

### nori 적용

es 에 nori 설치

```bash
> docker exec -it elaseicsearch bin/elasticsearch-plugin install analysis-nori
or
> docker exec -it elaseicsearch bash 
> bin/elasticsearch-plugin install analysis-nori

> docker restart elaseicsearch
```

PUT search-v2
```json
{
  "settings": {
    "index": {
      "number_of_shards": 1,
      "number_of_replicas": 0,
      "analysis": {
        "tokenizer": {
          "nori_user_dict": {
            "type": "nori_tokenizer",
            "decompound_mode": "mixed"
          }
        },
        "analyzer": {
          "korean": {
            "type": "custom",
            "tokenizer": "nori_user_dict",
            "filter": ["lowercase"]
          }
        }
      }
    }
  },
  "mappings": {
    "dynamic": "false",
    "properties": {
      "id": { "type": "long" },
      "productName": {
        "type": "text",
        "analyzer": "korean",
        "fields": {
          "keyword": { "type": "keyword", "ignore_above": 256 }
        }
      },
      "price": { "type": "long" },
      "quantity": { "type": "integer" },
      "description": { "type": "text", "analyzer": "korean" },
      "categoryId": { "type": "long" },
      "categoryName": {
        "type": "text",
        "analyzer": "korean",
        "fields": {
          "keyword": { "type": "keyword", "ignore_above": 256 }
        }
      },
      "indexedAt": { "type": "date", "format": "date_optional_time||epoch_millis" }
    }
  }
}
```
<br>

### Alias 적용

운영중 안정적으로 인덱스 변경을 위해 alias를 사용하여 인덱스 변경을 실시함   

POST _aliases
```
{
  "actions": [
    {
      "add": { "index": "search-v1", "alias": "search", "is_write_index": true }
    }
  ]
}
```

POST _aliases
```
{
  "actions": [
    {
        "remove": { "index": "search-v1", "alias": "search" }
        "add": { "index": "search-v2", "alias": "search", "is_write_index": true }
    }
  ]
}
```





