# workflow-template
Workflow (luồng công việc) là một tập hợp các hoạt động, công việc hoặc quy trình liên quan mà người hoặc hệ thống thực hiện để hoàn thành một nhiệm vụ cụ thể. 
Nó mô tả các bước cần thiết và quy trình thực hiện các công việc từ đầu đến cuối, bao gồm các loại công việc (dạng tuyến tính, dạng tree, dạng xử lý song song).

Ý tưởng chính :
Mỗi nghiệp vụ sẽ được chia làm 3 dạng cơ bản nhất, nó bao gồm
 + linear : dạng tuyến tính 
 + tree : dạng cây 
 + parallel : dạng song song

# Linear workflow :
    Linear workflow (luồng công việc tuyến tính) là một loại workflow trong đó các hoạt động được thực hiện theo một trình tự tuyến tính, từ bước này đến bước khác, không có sự nhánh và không có sự trùng lặp. Nghĩa là, sau khi hoàn thành một hoạt động, tiếp theo sẽ là hoạt động tiếp theo trong trình tự.
    Trong linear workflow, mỗi hoạt động được thực hiện sau khi hoạt động trước đó hoàn thành. Không có sự nhánh hoặc lựa chọn giữa các hoạt động khác nhau. Ví dụ, nếu có một linear workflow cho quy trình xử lý đơn hàng, các bước có thể là: Xác nhận đơn hàng, Xử lý thanh toán, Đóng gói sản phẩm và Gửi hàng. Các hoạt động này sẽ được thực hiện theo một trình tự tuyến tính, với mỗi hoạt động chỉ bắt đầu khi hoạt động trước đó hoàn thành.
    Linear workflow thường được sử dụng trong các quy trình đơn giản và tuyến tính, trong đó không có sự phụ thuộc phức tạp giữa các hoạt động và không có nhu cầu nhánh hoặc lựa chọn. Nó giúp đơn giản hóa quy trình và dễ dàng hiểu và theo dõi. Tuy nhiên, linear workflow có thể không phù hợp cho các quy trình phức tạp hơn, trong đó có các quyết định, nhánh hoặc lựa chọn phải được thực hiện.

## sample 
Ví dụ cho nghiệp vụ tạo 1 tài khoản
```sh
    @Component
    @AllArgsConstructor
    public class CreateNewAccountScenario {
      final InputAccountTask inputAccountTask;
      final AutoApproveAccountTask autoApproveAccountTask;
      final NotiTask notiTask;
    
      public AccountResponse doCreate(AccountContext context) {
    	// Bước 1 : Tạo tài khoản cá nhân
    	context.addTask(inputAccountTask);
    	// Bước 2 : Tự động duyệt thông tin
        context.addTask(autoApproveAccountTask)
    	// Bước 3 : Gửi thông báo 
    	context.addTask(notiTask);
      context.serialRun();
      return context.dataModel.getExtra(AccountResponse.class.getSimpleName(), AccountResponse.class);
      }
    }
```
# Tree workflow : 
    Tree workflow (luồng công việc cây) là một loại workflow trong đó các hoạt động được tổ chức thành một cấu trúc cây, với các nhánh và nút lá.
    Mỗi nút trong cây đại diện cho một hoạt động cụ thể trong quy trình, và các nhánh chỉ ra các mối quan hệ và trình tự giữa các hoạt động.
    Tree workflow thường được sử dụng trong các quy trình phức tạp hơn.
    Nó cung cấp một cấu trúc linh hoạt và dễ dàng mở rộng để quản lý các quy trình phức tạp và đảm bảo sự theo dõi và kiểm soát hiệu quả.
```sh
Dưới đây là một cấu trúc BPMN 
    +-------------------+
    |  Nhận yêu cầu     |
    +---------+---------+
              |
    +---------+---------+
    |                   |
    |  Kiểm tra yêu cầu |
    |                   |
    +---------+---------+
              |
       +------+------+
       |             |
  +----+----+   +----+----+
  |         |   |         |
  |Yêu cầu  |   |Yêu cầu  |
  |đúng     |   |sai      |
  |         |   |         |
  +----+----+   +----+----+
       |             |
    +--+-----+   +----+---+
    |        |   |        |
    |Xây dựng|   |Từ chối |
    |kế hoạch|   |dự án   |
    |        |   |        |
    +---+----+   +---+----+
        |           |
    +---+----+   +---+----+
    |        |   |        |
    |Đánh giá|   |Phát    |
    |dự án   |   |triển   |
    |        |   |sản phẩm|
    +--------+   +--------+
```
## sample 
```sh
Ví dụ về nghiệp vụ duyệt tài khoản
@Component
@AllArgsConstructor
public class ApproveScenario {
  final DetectAccountLiveTask detectAccountLiveTask; // Kiểm tra account có thỏa mãn để được duyệt
  final RejectTask rejectTask; // Task từ chối 
  final WriteHistoryTask writeHistoryTask; // Task ghi lịch sử 
  final NotiTask notiTask; // Task thông báo kết quả (sms,noti...)

  public AccountResponse doApprove(AccountContext context) {
	// Bước 1 : khởi tạo các node cho tree và node cho subtree , task đầu tiên được gọi là root task
	var rootTask = new TreeNodeTask<>(detectAccountLiveTask);
	var notiTreeTask = new TreeNodeTask<>(notiTask);
	
	// Cây xử lý nghiệp vụ từ chối
	var rejectTreeTask = new TreeNodeTask<>(rejectTask);
	// Sau khi từ chối thì gửi thông báo
	rejectTask.setRight(notiTask);
	
	// Cây xử lý ghi nhật kí thành công
    var writeHistoryTreeTask = new TreeNodeTask<>(writeHistoryTask);
	// Gửi thông báo sau khi duyệt thành công 
	writeHisTask.setRight(notiTask);
	
	// Bước 2 : Tạo kịch bản xử lý
	// Nếu account thỏa mãn thì chạy cây ghi nhật kí
    rootTask.setRight(writeHisTask);
	// Nếu account không thỏa mãn thì chạy cây từ chối
    rootTask.setLeft(rejectTask);
	
	
	var treeWorkFlow = new TreeWorkFlow<AccountContext>();
	// Bước 3 : Chạy mô hình
    treeWorkFlow.runOneTreeTask(rootTask, context);
	
    return context.dataModel.getExtra(AccountResponse.class.getSimpleName(),
        AccountResponse.class);
  }
}
```
# Parallel workflow :
Luồng công việc parallel (hay còn gọi là luồng công việc song song) là một loại luồng công việc trong quy trình kinh doanh mà các hoạt động được thực hiện đồng thời và độc lập với nhau.
Trong luồng công việc parallel, các hoạt động có thể được thực hiện đồng thời mà không cần chờ đợi hoàn thành của nhau. Điều này cho phép quy trình thực hiện nhiều công việc cùng một lúc,
giúp tăng tốc độ và hiệu suất của quy trình.
**_is being developed_**
