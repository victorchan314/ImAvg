# ImAvg: Image Averager

Inspired by Google Images and Samsung contact background colors, Image Averager computes the average RGB color of an image and creates a new image of that uniform color.

---

## Inspirations

- While Google Images is still loading images, it displays placeholder images of a uniform color, which I initially assumed was the average of those images
- Every Samsung contact can have an image; while the image is not in view, the background of the contact's page is of a color similar to the average of the contact's image

While both of the above revelations emerged as false, they were enough to compel me to create the program.

## Local Images

In order to average a local image, simply run the averager in your command line with the image as the argument.

    java ImAvg ../Halloween\ Ghost.jpg

## Remote Images

In order to average a remote image, simply run the averager in your command line with the image url as the argument to the `-u` flag.

    java ImAvg -u https://i.scdn.co/image/54969dd8c24693d05c8445c0de4ad74a719f1d65
