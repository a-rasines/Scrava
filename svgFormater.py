import sys

if(len(sys.argv) == 1):
    print("File needed")
    exit()
newLines = []
with(open(sys.argv[1], "r") as file):
    tabulations = -1
    for line in file.readlines():
        lenght = 0
        inString = False
        justTitle = False
        for section in line.replace("><", "> <").split(" "):
            concat = False
            if(len(section) == 0):
                continue
            elif "/>" in section or "</" in section:
                if inString:
                    newLines[-1] += " " + section
                else:
                    newLines.append("  " * tabulations + " " * lenght + section)
                inString = False
                tabulations -= 1
                print("▓tag end", section)
                continue
            elif section.startswith("<"):
                lenght = len(section) + 1
                tabulations += 1
                newLines.append("  " * tabulations + section)
                justTitle = True
                print("▒new tag", section, tabulations)
                continue
            elif "\""in section and section.index("\"") == section.rindex("\""):
                concat = inString
                inString = not inString
            else:
                concat = inString   
            if justTitle:
                concat = True
                justTitle = False
            print(section, concat, inString)
            if concat:
                newLines[-1] += " " + section
            else:
                newLines.append("  " * tabulations + " " * lenght + section)
with(open(sys.argv[1], "w") as file):
    end = "\n".join(newLines)
    while "\n\n" in end:
        end = end.replace("\n\n", "\n")
    file.write(end)
            