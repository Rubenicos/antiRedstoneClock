**Add Observer and Comparator support for spigot 1.12.2**
1. Download this two patches : [BlockObserver](./BlockObserver.patch) , [BlockRedstoneComparator](./BlockRedstoneComparator.patch)
2. java -jar BuildTools.jar --rev 1.12.2
3. go into CraftBukkit -> nms-patches and place both .patch file
4. java -jar BuildTools.jar --dont-update
5. You spigot is now patched to support Observer and Comparator