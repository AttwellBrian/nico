# Assemble
nasm -f macho test1.asm -o test1.o

# Link
ld -macosx_version_min 10.7.0 -o test1 test1.o

# Run
./test1

# Cleanup
rm test1.o test1