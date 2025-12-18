docker 실행
```bash
docker run -d --name test-db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=test -p 5432:5432 postgres:16
```

batch job 처음 실행해서 테이블 생성하고 -> enablebatchprocessing 실행시켜 커스텀 jobLauncher로 사용
