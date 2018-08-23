**Add Observer and Comparator support for spigot 1.12.2**
1. Download [BuildTools](https://hub.spigotmc.org/jenkins/job/BuildTools)
2. run ``java -jar BuildTools.jar --rev 1.12.2``
3. Download this two patches : [BlockObserver](./BlockObserver.patch) , [BlockRedstoneComparator](./BlockRedstoneComparator.patch)
4. go into ``CraftBukkit/nms-patches`` and place both ``.patch`` file into it
5. go back on the BuildTools.jar folder and run ``java -jar BuildTools.jar --dont-update``
6. You spigot is now patched to support Observer and Comparator