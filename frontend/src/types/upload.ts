export interface InitiateUploadRequest {
  fileName: string
  contentType: string
}

export interface PresignedUploadResponse {
  presignedUrl: string
  key: string
  expiresAt: string
}
