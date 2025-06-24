export interface InitiateUploadRequest {
  fileName: string
  contentType: string
  contentLength: number
}

export interface PresignedUploadResponse {
  presignedUrl: string
  key: string
  expiresAt: string
}
