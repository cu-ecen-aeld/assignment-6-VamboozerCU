#!/bin/sh
# Init scull script for the scull module

MODULE="scull"

#if [ "$(lsmod | grep $MODULE)" ]; then
#    echo "Unloading $MODULE module..."
#    rmmod $MODULE
#else
#    echo "Loading $MODULE module..."
#    modprobe $MODULE
#fi

case "$1" in
  start)
     echo "Loading $MODULE module..."
     #load_device
     if /usr/bin/scull_load ; then
        echo "Module $MODULE loaded successfully."
     else
        echo "Failed to load module $MODULE."
        #exit 1
     fi
     lsmod
     ;;
  stop)
     echo "Unloading $MODULE module..."
     #unload_device
     if /usr/bin/scull_unload ; then
        echo "Module $MODULE unloaded successfully."
     else
        echo "Failed to unload module $MODULE."
        #exit 1
     fi
     ;;
  force-reload|restart)
     echo "Reloading $MODULE module..."
     #unload_device
     #load_device
     if /usr/bin/scull_unload && /usr/bin/scull_load ; then
        echo "Module $MODULE reloaded successfully."
     else
        echo "Failed to reload module $MODULE."
        #exit 1
     fi
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|force-reload}"
     exit 1
esac

exit 0