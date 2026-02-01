import {useEffect, useState} from "react";

export const HOST = "http://-ycr-"

export function useYcrState<T>(id: string) {
  const url = `${HOST}state/get/${id}`
  const [value, setValue] = useState<T>(window.preload[id])

  function setValueAndPreload(value: T) {
    window.preload[id] = value
    setValue(value)
  }

  useEffect(() => {
    let cancelled = false
    window[`set_${id}`] = (uuid: string | null, value: T) => {
      cancelled = true
      if (uuid === window.uuid) return
      setValueAndPreload(value)
    }
    fetch(url)
      .then(response => response.json())
      .then(json => {
        if (!cancelled) setValueAndPreload(json)
      })
    return () => {
      cancelled = true
      delete window[`set_${id}`]
    }
  }, [id])

  return [value, (value: T) => {
    setValueAndPreload(value)
    void fetch(`${HOST}state/set/${id}`, {
      method: "POST",
      body: JSON.stringify(value)
    })
  }] as const
}
