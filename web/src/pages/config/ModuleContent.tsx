import {ModuleInfo} from "@/pages/config/ModulesPage.tsx";
import {useYcrState} from "@/api.tsx";
import React from "react";
import SwitchItem from "@/pages/config/item/SwitchItem.tsx";

export interface ConfigEntry {
  id: string | null,
  name: string | null,
  description: string | null,
  style: string,
  extra: any,
}

const styleToItem: Record<string, (props: { key: string, entry: ConfigEntry }) => React.ReactNode> = {
  "switch": SwitchItem
}

export default function ModuleContent({info}: { info: ModuleInfo }) {
  const [config] = useYcrState<ConfigEntry[]>(`${info.id}/config`)
  return <div className={"flex flex-col m-2"}>
    {
      config.map((entry, i) => {
        let Component = styleToItem[entry.style]
        return <Component key={`${i}`} entry={entry}/>
      })
    }
  </div>
}
