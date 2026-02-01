export {}

declare global {
  interface Window {
    [key: string]: any

    preload: Record<string, any>

    uuid: string
    scale: number
  }
}
