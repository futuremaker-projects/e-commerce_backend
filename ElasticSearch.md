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

```yml
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

```yml
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

```yml
POST _aliases
{
  "actions": [
    {
      "add": { "index": "search-v1", "alias": "search", "is_write_index": true }
    }
  ]
}
```

```yml
POST _aliases
{
  "actions": [
    {
        "remove": { "index": "search-v1", "alias": "search" },
        "add": { "index": "search-v2", "alias": "search", "is_write_index": true }
    }
  ]
}
```

### 보안설정

배포시 외부에서 접속해야하는 경우 kibana의 비밀번호를 설정하여 계정관리 해야함

```yml
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.18.1
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=true
      - ELASTIC_PASSWORD=${ElasticSearch_PASSWORD}         # kibana 관리자 페이지 접속시 사용하는 비번 
      - ES_JAVA_OPTS=-Xms512m -Xmx512m
      - TZ=Asia/Seoul
    ports:
      - "9200:9200"
    volumes:
      - .data/elasticsearch:/usr/share/elasticsearch/data
  kibana:
    image: docker.elastic.co/kibana/kibana:8.18.1
    container_name: kibana
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
      - ELASTICSEARCH_USERNAME=kibana_system          # Kibana가 Elasticsearch에 붙을 내장 계정(표준: kibana_system)
      - ELASTICSEARCH_PASSWORD=${KIBANA_PASSWORD}     # 리눅스 서버 내부에서 사용되는 명령어
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
```

kibana 프로세스가 백그라운드에서 elasticsearch Rest api 호출을 위해 사용하는 `서비스 계정`이다.
ELASTIC_PASSWORD와 ELASTICSEARCH_PASSWORD 는 동일해야 함

또한 kibana 관리자페이지에 접속 후 아래 계정을 사용하여 접속해야 한다
```
ID // PW : elastic // ELASTIC_PASSWORD 
```

> elastic 계정을 초기화 하고 싶다면
```sh
docker exec -it elasticsearch bin/elasticsearch-reset-password -i -u elastic
# 사용하지 않음
```


> kibana 관리자 페이지 접속이 안된다면 아래 명령어로 서버 관리자 계정을 초기화
```sh
docker exec -it elasticsearch bin/elasticsearch-reset-password -i -u kibana_system
# ELASTIC_PASSWORD 와 같은 비밀번호로 변경함

docker restart kibana 
```


보안설정 후 기존 volume 이 있다면 적용되지 않는다. volume 삭제 후 `docker compose -f elasticsearch.yml up -d` 명령어 실행
(elasticsearch volume 디렉토리 권한은 1000으로 설정, `sudo chown 1000:1000 ./data/elasticsearch`)

