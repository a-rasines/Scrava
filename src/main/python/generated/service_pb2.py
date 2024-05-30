# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: service.proto
# Protobuf Python Version: 5.26.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rservice.proto\"&\n\x05Query\x12\r\n\x05query\x18\x01 \x01(\t\x12\x0e\n\x06offset\x18\x02 \x01(\x05\")\n\x0bSudoMessage\x12\r\n\x05token\x18\x01 \x01(\t\x12\x0b\n\x03obj\x18\x02 \x01(\t\"9\n\x10SerializedObject\x12\n\n\x02id\x18\x01 \x01(\x05\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x0b\n\x03obj\x18\x03 \x01(\t\"*\n\nStringList\x12\x0b\n\x03ids\x18\x01 \x03(\t\x12\x0f\n\x07results\x18\x02 \x03(\t\"!\n\x0c\x43ipherUpdate\x12\x11\n\tpublicKey\x18\x01 \x01(\x0c\"5\n\nClientData\x12\n\n\x02id\x18\x01 \x01(\x05\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\r\n\x05token\x18\x03 \x01(\t\"-\n\x0b\x43lientLogin\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x10\n\x08password\x18\x02 \x01(\t\"?\n\x0e\x43lientRegister\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x10\n\x08password\x18\x02 \x01(\t\x12\r\n\x05\x65mail\x18\x03 \x01(\t\"\"\n\x0eStringResponse\x12\x10\n\x08response\x18\x01 \x01(\t\"\x0e\n\x0c\x45mptyMessage2\x96\x03\n\x06Scrava\x12\x31\n\x0fstartConnection\x12\r.EmptyMessage\x1a\r.CipherUpdate\"\x00\x12$\n\x05login\x12\x0c.ClientLogin\x1a\x0b.ClientData\"\x00\x12*\n\x08register\x12\x0f.ClientRegister\x1a\x0b.ClientData\"\x00\x12,\n\x0bsaveProject\x12\x0c.SudoMessage\x1a\r.EmptyMessage\"\x00\x12(\n\x0fgetTutorialList\x12\x06.Query\x1a\x0b.StringList\"\x00\x12\'\n\x0egetProjectList\x12\x06.Query\x1a\x0b.StringList\"\x00\x12/\n\rrefreshCypher\x12\r.EmptyMessage\x1a\r.CipherUpdate\"\x00\x12*\n\x0bgetTutorial\x12\x06.Query\x1a\x11.SerializedObject\"\x00\x12)\n\ngetProject\x12\x06.Query\x1a\x11.SerializedObject\"\x00\x42\x15\n\x06serverB\x0bScravaProtob\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'service_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\006serverB\013ScravaProto'
  _globals['_QUERY']._serialized_start=17
  _globals['_QUERY']._serialized_end=55
  _globals['_SUDOMESSAGE']._serialized_start=57
  _globals['_SUDOMESSAGE']._serialized_end=98
  _globals['_SERIALIZEDOBJECT']._serialized_start=100
  _globals['_SERIALIZEDOBJECT']._serialized_end=157
  _globals['_STRINGLIST']._serialized_start=159
  _globals['_STRINGLIST']._serialized_end=201
  _globals['_CIPHERUPDATE']._serialized_start=203
  _globals['_CIPHERUPDATE']._serialized_end=236
  _globals['_CLIENTDATA']._serialized_start=238
  _globals['_CLIENTDATA']._serialized_end=291
  _globals['_CLIENTLOGIN']._serialized_start=293
  _globals['_CLIENTLOGIN']._serialized_end=338
  _globals['_CLIENTREGISTER']._serialized_start=340
  _globals['_CLIENTREGISTER']._serialized_end=403
  _globals['_STRINGRESPONSE']._serialized_start=405
  _globals['_STRINGRESPONSE']._serialized_end=439
  _globals['_EMPTYMESSAGE']._serialized_start=441
  _globals['_EMPTYMESSAGE']._serialized_end=455
  _globals['_SCRAVA']._serialized_start=458
  _globals['_SCRAVA']._serialized_end=864
# @@protoc_insertion_point(module_scope)
