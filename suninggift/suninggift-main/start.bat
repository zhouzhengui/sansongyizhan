@echo off

timeout 1 > NUL & start cmd /c "title suninggift-demo(16003) & java -Xms512m -Xmx1024m -Dskywalking.agent.namespace=tdev -Dskywalking.agent.service_name=tdev-suninggift -Dskywalking.agent.sample_n_per_3_secs=200 -javaagent:C:\Users\huang\Documents\agent-hz\skywalking-agent.jar -jar target\suninggift-main-1.0.0-SNAPSHOT.jar --server.port=16003"
