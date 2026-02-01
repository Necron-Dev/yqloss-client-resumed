import {Card, Description, Label, Switch, SwitchControl, SwitchThumb} from "@heroui/react";
import {useYcrState} from "@/api.tsx";

export default function ModuleCard({id, name, description}: { id: string, name: string, description: string }) {
  const [enabled, setEnabled] = useYcrState<boolean>(`${id}/enabled`)

  return <Card className={"bg-background-secondary shrink-0 rounded-2xl"}>
    <div className={"flex flex-row justify-between"}>
      <div className={"flex flex-col"}>
        <Label className={"text-[1rem]"}>{name}</Label>
        <Description>{description}</Description>
      </div>
      <Switch isSelected={enabled} onChange={on => setEnabled(on)}>
        <SwitchControl>
          <SwitchThumb/>
        </SwitchControl>
      </Switch>
    </div>
  </Card>
}
