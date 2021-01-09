//
//  Position.h
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

@interface Position : NSObject {
    int idx;
    int ln;
    int col;
    NSString* fn;
    NSString* ftxt;
}

- (Position*) init:(int) idx line:(int) ln column:(int) col fileName:(NSString*) fn fileText:(NSString*) ftxt;

- (Position*) advance: (char) currentChar;
- (Position*) advance;
- (Position*) clone;
- (int) getLine;
- (NSString*) getFileName;

@end
