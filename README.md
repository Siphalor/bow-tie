# Bow Tie

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
