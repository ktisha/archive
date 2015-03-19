#ifndef PDFGENERATOR_H
#define PDFGENERATOR_H

#include <QString>
#include "filelist.h"

QString pdfGenerate(const QString & fileName, const FileList & fileList);

#endif // PDFGENERATOR_H
