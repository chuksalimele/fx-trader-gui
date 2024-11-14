/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.exception;

/**
 *
 * @author user
 */
public interface ExpertErrorDesc {
    
    static String toString(int error_code){        
        return switch (error_code) {
            case 0 ->
                "no error";
            case 1 ->
                "no error, trade conditions not changed";
            case 2 ->
                "common error";
            case 3 ->
                "invalid trade parameters";
            case 4 ->
                "trade server is busy";
            case 5 ->
                "old version of the client terminal";
            case 6 ->
                "no connection with trade server";
            case 7 ->
                "not enough rights";
            case 8 ->
                "too frequent requests";
            case 9 ->
                "malfunctional trade operation (never returned error)";
            case 64 ->
                "account disabled";
            case 65 ->
                "invalid account";
            case 128 ->
                "trade timeout";
            case 129 ->
                "invalid price";
            case 130 ->
                "invalid stops";
            case 131 ->
                "invalid trade volume";
            case 132 ->
                "market is closed";
            case 133 ->
                "trade is disabled";
            case 134 ->
                "not enough money";
            case 135 ->
                "price changed";
            case 136 ->
                "off quotes";
            case 137 ->
                "broker is busy (never returned error)";
            case 138 ->
                "requote";
            case 139 ->
                "order is locked";
            case 140 ->
                "long positions only allowed";
            case 141 ->
                "too many requests";
            case 145 ->
                "modification denied because order is too close to market";
            case 146 ->
                "trade context is busy";
            case 147 ->
                "expirations are denied by broker";
            case 148 ->
                "amount of open and pending orders has reached the limit";
            case 149 ->
                "hedging is prohibited";
            case 150 ->
                "prohibited by FIFO rules";
            case 4000 ->
                "no error (never generated code)";
            case 4001 ->
                "wrong function pointer";
            case 4002 ->
                "array index is out of range";
            case 4003 ->
                "no memory for function call stack";
            case 4004 ->
                "recursive stack overflow";
            case 4005 ->
                "not enough stack for parameter";
            case 4006 ->
                "no memory for parameter string";
            case 4007 ->
                "no memory for temp string";
            case 4008 ->
                "non-initialized string";
            case 4009 ->
                "non-initialized string in array";
            case 4010 ->
                "no memory for array\' string";
            case 4011 ->
                "too long string";
            case 4012 ->
                "remainder from zero divide";
            case 4013 ->
                "zero divide";
            case 4014 ->
                "unknown command";
            case 4015 ->
                "wrong jump (never generated error)";
            case 4016 ->
                "non-initialized array";
            case 4017 ->
                "dll calls are not allowed";
            case 4018 ->
                "cannot load library";
            case 4019 ->
                "cannot call function";
            case 4020 ->
                "expert function calls are not allowed";
            case 4021 ->
                "not enough memory for temp string returned from function";
            case 4022 ->
                "system is busy (never generated error)";
            case 4023 ->
                "dll-function call critical error";
            case 4024 ->
                "internal error";
            case 4025 ->
                "out of memory";
            case 4026 ->
                "invalid pointer";
            case 4027 ->
                "too many formatters in the format function";
            case 4028 ->
                "parameters count is more than formatters count";
            case 4029 ->
                "invalid array";
            case 4030 ->
                "no reply from chart";
            case 4050 ->
                "invalid function parameters count";
            case 4051 ->
                "invalid function parameter value";
            case 4052 ->
                "string function internal error";
            case 4053 ->
                "some array error";
            case 4054 ->
                "incorrect series array usage";
            case 4055 ->
                "custom indicator error";
            case 4056 ->
                "arrays are incompatible";
            case 4057 ->
                "global variables processing error";
            case 4058 ->
                "global variable not found";
            case 4059 ->
                "function is not allowed in testing mode";
            case 4060 ->
                "function is not confirmed";
            case 4061 ->
                "send mail error";
            case 4062 ->
                "string parameter expected";
            case 4063 ->
                "integer parameter expected";
            case 4064 ->
                "double parameter expected";
            case 4065 ->
                "array as parameter expected";
            case 4066 ->
                "requested history data is in update state";
            case 4067 ->
                "internal trade error";
            case 4068 ->
                "resource not found";
            case 4069 ->
                "resource not supported";
            case 4070 ->
                "duplicate resource";
            case 4071 ->
                "cannot initialize custom indicator";
            case 4072 ->
                "cannot load custom indicator";
            case 4073 ->
                "no history data";
            case 4074 ->
                "not enough memory for history data";
            case 4075 ->
                "not enough memory for indicator";
            case 4099 ->
                "end of file";
            case 4100 ->
                "some file error";
            case 4101 ->
                "wrong file name";
            case 4102 ->
                "too many opened files";
            case 4103 ->
                "cannot open file";
            case 4104 ->
                "incompatible access to a file";
            case 4105 ->
                "no order selected";
            case 4106 ->
                "unknown symbol";
            case 4107 ->
                "invalid price parameter for trade function";
            case 4108 ->
                "invalid ticket";
            case 4109 ->
                "trade is not allowed in the expert properties";
            case 4110 ->
                "longs are not allowed in the expert properties";
            case 4111 ->
                "shorts are not allowed in the expert properties";
            case 4200 ->
                "object already exists";
            case 4201 ->
                "unknown object property";
            case 4202 ->
                "object does not exist";
            case 4203 ->
                "unknown object type";
            case 4204 ->
                "no object name";
            case 4205 ->
                "object coordinates error";
            case 4206 ->
                "no specified subwindow";
            case 4207 ->
                "graphical object error";
            case 4210 ->
                "unknown chart property";
            case 4211 ->
                "chart not found";
            case 4212 ->
                "chart subwindow not found";
            case 4213 ->
                "chart indicator not found";
            case 4220 ->
                "symbol select error";
            case 4250 ->
                "notification error";
            case 4251 ->
                "notification parameter error";
            case 4252 ->
                "notifications disabled";
            case 4253 ->
                "notification send too frequent";
            case 4260 ->
                "ftp server is not specified";
            case 4261 ->
                "ftp login is not specified";
            case 4262 ->
                "ftp connect failed";
            case 4263 ->
                "ftp connect closed";
            case 4264 ->
                "ftp change path error";
            case 4265 ->
                "ftp file error";
            case 4266 ->
                "ftp error";
            case 5001 ->
                "too many opened files";
            case 5002 ->
                "wrong file name";
            case 5003 ->
                "too long file name";
            case 5004 ->
                "cannot open file";
            case 5005 ->
                "text file buffer allocation error";
            case 5006 ->
                "cannot delete file";
            case 5007 ->
                "invalid file handle (file closed or was not opened)";
            case 5008 ->
                "wrong file handle (handle index is out of handle table)";
            case 5009 ->
                "file must be opened with FILE_WRITE flag";
            case 5010 ->
                "file must be opened with FILE_READ flag";
            case 5011 ->
                "file must be opened with FILE_BIN flag";
            case 5012 ->
                "file must be opened with FILE_TXT flag";
            case 5013 ->
                "file must be opened with FILE_TXT or FILE_CSV flag";
            case 5014 ->
                "file must be opened with FILE_CSV flag";
            case 5015 ->
                "file read error";
            case 5016 ->
                "file write error";
            case 5017 ->
                "string size must be specified for binary file";
            case 5018 ->
                "incompatible file (for string arrays-TXT, for others-BIN)";
            case 5019 ->
                "file is directory, not file";
            case 5020 ->
                "file does not exist";
            case 5021 ->
                "file cannot be rewritten";
            case 5022 ->
                "wrong directory name";
            case 5023 ->
                "directory does not exist";
            case 5024 ->
                "specified file is not directory";
            case 5025 ->
                "cannot delete directory";
            case 5026 ->
                "cannot clean directory";
            case 5027 ->
                "array resize error";
            case 5028 ->
                "string resize error";
            case 5029 ->
                "structure contains strings or dynamic arrays";
                
            case 7000 ->
                "Symbol not found";
                
            case 7001 ->
                "Order not found";
                                
            case 7002 ->
                "Unknown order type";
                                
            case 7003 ->
                "Order not selected";
                                
            default ->
                "unknown error";
        }; //--- codes returned from trade server
        //--- mql4 errors
    }
}
