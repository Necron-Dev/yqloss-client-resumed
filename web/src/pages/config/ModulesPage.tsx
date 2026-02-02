import {ScrollShadow} from "@heroui/react";
import {useState} from "react";
import ModulesContent from "@/pages/config/ModulesContent.tsx";
import ModuleContent from "@/pages/config/ModuleContent.tsx";
import {AnimatePresence, motion} from "framer-motion";

export interface ModuleInfo {
  id: string,
  name: string,
  description: string
}

export default function ModulesPage({setTitle}: { setTitle: (title: string | undefined) => void }) {
  const [info, setInfo] = useState<ModuleInfo | undefined>(undefined)

  function setInfoAndTitle(moduleInfo: ModuleInfo | undefined) {
    setInfo(moduleInfo)
    setTitle(moduleInfo?.name)
  }

  return <AnimatePresence mode={"wait"}>
    <motion.div
      className={"h-full"}
      key={`content-${info?.id}`}
      initial={{opacity: 0, y: "2rem"}}
      animate={{opacity: 1, y: 0}}
      exit={{opacity: 0, y: "2rem"}}
      transition={{duration: 0.1, ease: "easeOut"}}
    >
      <ScrollShadow hideScrollBar className={"h-full rounded-2xl"} visibility={"none"}>
        {
          info === undefined
            ? <ModulesContent setInfo={setInfoAndTitle}/>
            : <ModuleContent info={info}/>
        }
      </ScrollShadow>
    </motion.div>
  </AnimatePresence>
}
