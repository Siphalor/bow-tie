# Bow Tie


[![curseforge downloads](http://cf.way2muchnoise.eu/full_bow-tie_downloads.svg)](https://minecraft.curseforge.com/projects/bow-tie)
[![curseforge mc versions](http://cf.way2muchnoise.eu/versions/bow-tie.svg)](https://minecraft.curseforge.com/projects/bow-tie)
[![modrinth downloads](https://img.shields.io/badge/dynamic/json?color=5da545&label=modrinth&prefix=%20&query=&url=https://api.modrinth.com/api/v1/mod/A0bpnHwe&style=flat&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/bow-tie)

![logo](logo.png?raw=true)

> Did you ever get annoyed by not being able to write comments in your Minecraft JSON files?
> 
> Do you want whitespace to decide on your data nesting?
> 
> Did you ever want to write your recipes as lovely XML compared to JSON?

Then I got the right thing for you :)

---

This Minecraft Fabric mod uses the data API of [Tweed 4](https://github.com/Siphalor/tweed-api).

You can use all data formats that Tweed supports to replace your JSON mess.
Supported are:

- [Hjson](https://hjson.github.io/)
- [JSON5](https://json5.org/)
- [YAML](https://yaml.org/)
- XML

## Examples

Example files formulated in alternate data formats can be found [in the testmod](https://github.com/Siphalor/bow-tie/tree/1.16/src/testmod/resources).

## How does it work?

*Bow Tie* tries to intercept all data loading and checks for supported files with the same name.
It then loads these files with the respective serializers and uses Tweed's API to convert them to JSON files.
These are then handed back to the game.
