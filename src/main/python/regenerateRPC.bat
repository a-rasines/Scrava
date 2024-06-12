@echo off
call C:\Users\HP\virtual_envs\tfg\Scripts\activate.bat
python -m grpc_tools.protoc -I../proto --python_out=. --grpc_python_out=. ../proto/service.proto
move service_pb2.py generated
move service_pb2_grpc.py generated
powershell -Command "(gc generated/service_pb2_grpc.py) -replace 'import service_pb2', 'import generated.service_pb2' | Out-File -encoding ASCII generated/service_pb2_grpc.py"
pause