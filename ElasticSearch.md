## Elasticsearch 검색용 색인을 위한 배치 구성

한글 색인을 위한 elasticsearch 색인 설정 및 초기 인덱스 설정

### nori 적용

es 에 nori 설치(docker)

```bash
> docker exec -it elasticsearch bin/elasticsearch-plugin install analysis-nori
or
> docker exec -it elasticsearch bash 
> bin/elasticsearch-plugin install analysis-nori

> docker restart elasticsearch
```

### 인덱스

#### 인덱스 생성 명령어(nori 포함)

```
PUT search-v1
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

### standard 적용

```
PUT search-v1
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

#### 인덱스에 적용된 settings 확인

GET [인덱스명]/_settings
```
GET search-v2/_settings
```

#### 데이터 수량 파악

GET [인덱스명]/_count
```
POST search-v1/_delete_by_query
```

#### 인덱스 삭제

POST [인덱스명]/_delete_by_query
```
POST search-v1/_delete_by_query
```

#### Alias 적용

운영중 안정적으로 인덱스 변경을 위해 alias를 사용하여 인덱스 변경을 실시함

```
POST _aliases
{
  "actions": [
    {
      "add": { "index": "search-v1", "alias": "search", "is_write_index": true }
    }
  ]
}
```

```
POST _aliases
{
  "actions": [
    {
        "remove": { "index": "search-v1", "alias": "search" }
        "add": { "index": "search-v2", "alias": "search", "is_write_index": true }
    }
  ]
}
```





