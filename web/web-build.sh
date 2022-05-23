BUILD_DIR="dist/ibs"

cd ../web
mkdir -p ${BUILD_DIR} ../main/target/classes/templates ../main/target/classes/static
npm i
npm run build
if [ -d "${BUILD_DIR}" ]; then
  cd ${BUILD_DIR}
  mv index.html ../../../main/target/classes/templates
  cp -r * ../../../main/target/classes/static
else
  echo "dist/ibs not exists"
  exit 1
fi
