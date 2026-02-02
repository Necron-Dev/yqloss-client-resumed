import {ConfigEntry} from "@/pages/config/ModuleContent.tsx";
import {Description, Label, Switch, SwitchControl} from "@heroui/react";
import {useYcrState} from "@/api.tsx";

export default function SwitchItem({entry}: { entry: ConfigEntry }) {
  const [value, setValue] = useYcrState<boolean>(entry.id!);

  return <Switch className={"justify-between items-center"} isSelected={value} onChange={setValue}>
    <div className={"flex flex-col"}>
      <Label>{entry.name}</Label>
      {entry.description && <Description>{entry.description}</Description>}
    </div>

    <SwitchControl>
      <Switch.Thumb/>
    </SwitchControl>
  </Switch>
}
