import {useEffect, useState} from "react";
import {Button} from "@heroui/react"
import Located, {at} from "@/Located.tsx";
import {motion} from "framer-motion";

export default function HudPage() {
  const [fps, setFps] = useState(0);

  useEffect(() => {
    window.setFps = (newFps: number) => {
      setFps(newFps);
    }
  })

  return <Located location={at(0.5, 0.5, 0.5, 0, 0, 10)}>
    <motion.div initial={{scale: 0}} animate={{scale: 1}}>
      <Button>
        FPS: {fps}
      </Button>
    </motion.div>
  </Located>
}
