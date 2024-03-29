#include "common/windows/sym_upload_v2_protocol.h"

#include <cstdio>

#include "common/windows/http_upload.h"
#include "common/windows/symbol_collector_client.h"

using google_breakpad::CompleteUploadResult;
using google_breakpad::HTTPUpload;
using google_breakpad::SymbolCollectorClient;
using google_breakpad::SymbolStatus;
using google_breakpad::UploadUrlResponse;
using std::wstring;

namespace google_breakpad {

static bool SymUploadV2ProtocolSend(const wchar_t* api_url,
                                    const wchar_t* api_key,
                                    int* timeout_ms,
                                    const wstring& debug_file,
                                    const wstring& debug_id,
                                    const wstring& symbol_filename,
                                    const wstring& symbol_type,
                                    const wstring& product_name,
                                    bool force) {
  wstring url(api_url);
  wstring key(api_key);

  if (!force) {
    SymbolStatus symbolStatus = SymbolCollectorClient::CheckSymbolStatus(
        url, key, timeout_ms, debug_file, debug_id);
    if (symbolStatus == SymbolStatus::Found) {
      wprintf(
          L"Symbol file already exists, upload aborted."
          L" Use \"-f\" to overwrite.\n");
      return true;
    } else if (symbolStatus == SymbolStatus::Unknown) {
      wprintf(L"Failed to get check for existing symbol.\n");
      return false;
    }
  }

  UploadUrlResponse uploadUrlResponse;
  if (!SymbolCollectorClient::CreateUploadUrl(url, key, timeout_ms,
                                              &uploadUrlResponse)) {
    wprintf(L"Failed to create upload URL.\n");
    return false;
  }

  wstring signed_url = uploadUrlResponse.upload_url;
  wstring upload_key = uploadUrlResponse.upload_key;
  wstring response;
  int response_code;
  bool success = HTTPUpload::SendPutRequest(
      signed_url, symbol_filename, timeout_ms, &response, &response_code);
  if (!success) {
    wprintf(L"Failed to send symbol file.\n");
    wprintf(L"Response code: %ld\n", response_code);
    wprintf(L"Response:\n");
    wprintf(L"%s\n", response.c_str());
    return false;
  } else if (response_code == 0) {
    wprintf(L"Failed to send symbol file: No response code\n");
    return false;
  } else if (response_code != 200) {
    wprintf(L"Failed to send symbol file: Response code %ld\n", response_code);
    wprintf(L"Response:\n");
    wprintf(L"%s\n", response.c_str());
    return false;
  }

  CompleteUploadResult completeUploadResult =
      SymbolCollectorClient::CompleteUpload(url, key, timeout_ms, upload_key,
                                            debug_file, debug_id, symbol_type,
                                            product_name);
  if (completeUploadResult == CompleteUploadResult::Error) {
    wprintf(L"Failed to complete upload.\n");
    return false;
  } else if (completeUploadResult == CompleteUploadResult::DuplicateData) {
    wprintf(
        L"Uploaded file checksum matched existing file checksum,"
        L" no change necessary.\n");
  } else {
    wprintf(L"Successfully sent the symbol file.\n");
  }

  return true;
}

}  // namespace google_breakpad