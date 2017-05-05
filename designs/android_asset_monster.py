import glob
import sys
from shutil import copy

src = sys.argv[1]
asset = sys.argv[2]
dest = sys.argv[3]


def prepare_destination(source, dest_parent, resolution):
    return dest_parent + "/mipmap-" + resolution + "/"+source.split('/')[-1].split("-" + resolution)[0]+".png"



print "Searching", src + "/" + asset + "*.png"

files = glob.glob(pathname=src + "/" + asset + "*.png")
print files

for f in files:
    resolution = f.split('/')[-1].split("-")[-1].split('.')[0]
    destination = prepare_destination(source=f, dest_parent=dest, resolution=resolution)
    copy(src=f, dst=destination)
    print f, "copied to", destination
