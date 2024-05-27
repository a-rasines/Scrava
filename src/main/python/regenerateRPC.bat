REM workon tfg
call C:\Users\HP\virtual_envs\tfg\Scripts\activate.bat
python -m grpc_tools.protoc -I../proto --python_out=. --grpc_python_out=. ../proto/service.proto
pause