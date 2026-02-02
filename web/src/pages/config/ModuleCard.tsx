import {Card, Description, Label, Switch, SwitchControl, SwitchThumb} from "@heroui/react";
import {useYcrState} from "@/api.tsx";
import {ModuleInfo} from "@/pages/config/ModulesPage.tsx";

export default function ModuleCard({info, setInfo}: {
  info: ModuleInfo
  setInfo: (value: ModuleInfo) => void
}) {
  const [enabled, setEnabled] = useYcrState<boolean>(`${info.id}/enabled`)

  return <Card className={"bg-background-secondary shrink-0 rounded-2xl"} onClick={() => setInfo(info)}>
    <div className={"flex flex-row justify-between"}>
      <div className={"flex flex-col"}>
        <Label className={"text-[1rem]"}>{info.name}</Label>
        <Description>{info.description}</Description>
      </div>
      <Switch isSelected={enabled} onChange={on => setEnabled(on)}>
        <SwitchControl>
          <SwitchThumb/>
        </SwitchControl>
      </Switch>
    </div>
  </Card>
}
