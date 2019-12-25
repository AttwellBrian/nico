# Assemble
nasm -f macho test1.asm -o test1.o

# Link. Using "-no_pie" per warning outputted by ld.
ld -macosx_version_min 10.7.0 -no_pie -o test1 test1.o

# Run
./test1

# Cleanup
rm test1.o test1